app_controller = ae.ARApplicationController:shared_instance()
app_controller:require('./scripts/include.lua')
app = AR:create_application(AppType.Slam, 'constraint')
app.device:open_imu(1)

app:load_scene_from_json("res/simple_scene.json","demo_scene")
scene = app:get_current_scene()

local TYPE_RED = 1
local TYPE_BLUE = 2
local TYPE_GREEN = 3

local debug_mode = 0
local DBG_ALL = -1
local DBG_NoDebug = 0
local DBG_DrawWireframe = 1
local DBG_DrawAabb = 2
local DBG_DrawConstraints = 2048
local DBG_DrawConstraintLimits = 4096

--const
local HALF_BOX_LENGTH = 0
local TOTAL_BALL_INDEX = 0
local BALL_ALLOW_LIVINGTIME = 7000

local VELOCITY_SCALE = 800
local IMPLUSE_SCALE = 1000
--const

-- status
local allow_shot = false
-- status

-- case user data
local now_ball_node = {}
local all_cloned_balls = {}
local all_cloned_brick = {}
-- case user data


--game environment
local const_game_time = 60000
local game_time = const_game_time
local score = 0
local gamestart = false

local gamediff = {_type = "kinematic", _mass = 0.5}
--game environment

app.on_loading_finish = function()
    ARLOG('loading finish')
    app:visible_type(ViewVisibleType.HideShotButton)

    initilize_game_environment()
end

function initilize_game_environment()
    initialize_game()
    initialize_html()
    initialize_physics()
end

function initialize_game()
    --初始化全局变量
    local box_red = scene.brick_red:get_bounding_box()
    local half_red = box_red:half_extent()
    HALF_BOX_LENGTH = half_red.x
    root = scene:get_root_node() 

    --设置update响应
    scene:set_update_handler(engine_update)
end

function initialize_physics()
    --初始化物理世界
    gravity = ae.ARVec3:new(0,-250,0)
    scene:create_physics_world(gravity)
    physics_world = scene:get_physics_world()
    physics_world:set_debug_draw_mode(DBG_DrawWireframe + DBG_DrawConstraints + DBG_DrawConstraintLimits)
    scene.floor:create_physics_body(0.0, 0.0, 1.0,"static", "box")
end

function initialize_html()

    html_score = scene.score_html:webview()
                            :width(1000)
                            :height(1800)
                            :is_remote(0)
                            :url('res/webroot/score.html')
                            :load()

    html_time = scene.time_html:webview()
                            :width(1000)
                            :height(1800)
                            :is_remote(0)
                            :url('res/webroot/time.html')
                            :load()

    html_score.on_load_finish = function()
        scene.score_html:set_property_bool("visible",true)
    end

    html_time.on_load_finish = function()
        scene.time_html:set_property_bool("visible",true)
    end

end

function start_game()

    scene.gameover:set_property_bool("visible",false)
    scene.aim:set_property_bool("visible",true)
    diff_choose_button(false)
    add_all_box()
    prepare_ball_and_button()
    reset_param()
end

function reset_param()
    score = 0
    local string = "score: "..tostring(score)
    html_score:update_js("changeword(".."'"..string.."'"..")")

    game_time = const_game_time
    gamestart = true
end

function diff_choose_button(ok)
    scene.title:set_property_bool("visible",ok)
    scene.normal:set_property_bool("visible",ok)
    scene.hard:set_property_bool("visible",ok)
    scene.crazy:set_property_bool("visible",ok)
end

function add_all_box()
    add_box(0)
    add_box(1)
    add_box(2)
end

function add_box(floor_index)

    for i=1, 9 do

    	local casetype = math.random(1,3)

    	local name = "brick_red"

    	if (casetype == TYPE_RED) then
    		name = "brick_red"
    	end

    	if (casetype == TYPE_BLUE) then
    		name = "brick_blue"
    	end

    	if (casetype == TYPE_GREEN) then
    		name = "brick_green"
    	end

    	local node = scene:clone_node(name, tostring(i + 9 * floor_index))
    	root:add_sub_node(node)
    	node:set_property_bool("visible", true)
    	local x,y,z = create_position(i, floor_index)
    	local position = ae.ARVec3:new_local(x,y,z)
    	node:set_property_vec3("world_position",position)
        node:create_physics_body(gamediff._mass, 0.01, 10,gamediff._type, "box")
        --node:create_physics_body(2.0, 0.01, 10,"dynamic", "box")

        local brick = { brick_name = node:get_property_string("name"), brick_type = casetype}
        table.insert(all_cloned_brick, brick)
    end
end

function create_position(i, floor_index)

	local x = 0
	local y = 0
	local z = 0

	if (floor_index == 0) then
		y = 0
	end

	if (floor_index == 1) then
		y = HALF_BOX_LENGTH * 2
	end

	if (floor_index == 2) then
		y = HALF_BOX_LENGTH * 4
	end

    if (i < 4) then
        z = HALF_BOX_LENGTH * 2
    end

    if (i > 3 and i < 7) then
        z = 0
    end

    if (i > 6 and i < 10) then
        z = HALF_BOX_LENGTH * -2
    end

    if (i == 1 or i == 4 or i == 7) then
    	x = HALF_BOX_LENGTH * -2
    end

    if (i == 2 or i == 5 or i == 8) then
    	x = 0
    end

    if (i == 3 or i == 6 or i == 9) then
    	x = HALF_BOX_LENGTH * 2
    end
    
	return x,y,z
end

function prepare_ball_and_button()

    local casetype = math.random(1,3)

    local length = table.getn(all_cloned_brick)

    if (length > 0) then
        local index = math.random(1,length)
        casetype = all_cloned_brick[index].brick_type
    end
    ARLOG("ball type "..tostring(length).." "..tostring(casetype))
    

    replace_shot_button(casetype)
    add_random_ball(casetype)

    allow_shot = true
end

function replace_shot_button(type)

    local shot_button = "res/texture/red.png"

    scene.button_shot:set_property_bool("visible",true)

    if (type == TYPE_RED) then
        shot_button = "res/texture/red.png"
    end

    if (type == TYPE_BLUE) then
        shot_button = "res/texture/blue.png"
    end

    if (type == TYPE_GREEN) then
        shot_button = "res/texture/green.png"
    end

    scene.button_shot:replace_texture(shot_button, "uDiffuseTexture")
end

function add_random_ball(type)

    local ball_name = "ball_red"

    if (type == TYPE_RED) then
        ball_name = "ball_red"
    end

    if (type == TYPE_BLUE) then
       ball_name = "ball_blue"
    end

    if (type == TYPE_GREEN) then
        ball_name = "ball_green"
    end

    now_ball_node = scene:clone_node(ball_name, tostring(TOTAL_BALL_INDEX))
    TOTAL_BALL_INDEX = TOTAL_BALL_INDEX + 1
    root:add_sub_node(now_ball_node)
    --now_ball_node:set_property_bool("visible", true)
end


function shot_ball()

    if(allow_shot) then

        local cloned_ball = { entity = now_ball_node, livetime = 0}
        table.insert(all_cloned_balls, cloned_ball)

        local camera = scene:get_active_camera()
        local camera_pos = camera:get_property_vec3("world_position")
        now_ball_node:set_property_bool("visible", true)
        now_ball_node:set_property_vec3("world_position", camera_pos)
        now_ball_node:create_physics_body(0.5, 0.5, 10,"dynamic", "sphere")
        local physics_body = now_ball_node:get_physics_body()
        local screen_pos = app:get_screen_size() * ae.ARVec2:new_local(0.5, 0.5)
        local dest = camera:unproject(screen_pos, 1000)
        local impluse = (dest - camera_pos):normalize() * 1.8 * VELOCITY_SCALE

        --ARLOG("camera_pos "..camera_pos:to_string())
        --ARLOG("dest "..dest:to_string())
        --ARLOG("implsue "..impluse:to_string())
        physics_body:set_property_vec3("impluse", impluse)
        physics_body:set_collision_handler(ball_collision_handler)
        --scene.button_shot:replace_texture("res/texture/grey.png", "uDiffuseTexture")
        play_shot_music(now_ball_node)
    end
end

function play_shot_music(node)
    local media_controller = node:get_media_controller()
    local config = {}
    config["repeat_count"] = 1
    config["delay"] = 0
    local media_session = media_controller:create_media_session("audio", "res/media/ball_fly_out.mp3", config)
    media_session:play()
end

scene.button_shot.on_click = function()

    if (not gamestart) then
        return
    end

    shot_ball()
    prepare_ball_and_button()
end


function engine_update(delta)

    if (not gamestart) then
        return
    end

    --ARLOG("engine_update"..tostring(delta))
    --删除球体
    for i,v in pairs(all_cloned_balls) do

        --print(i,v)
        v.livetime = v.livetime + delta

        if (v.livetime > BALL_ALLOW_LIVINGTIME) then
            local name = v.entity:get_property_string("name")
            --ARLOG("remove node name "..name)
            scene:remove_node_by_name(name)
            table.remove(all_cloned_balls, i)
        end
    end

    if (scene.time_html:get_property_bool("visible")) then
        update_time(delta)
    end
    
end

function update_time(delta)
    game_time = game_time - delta

    if (game_time < 0 ) then
        game_time = 0

        local string = "game end！！！！"
        html_time:update_js("changeword(".."'"..string.."'"..")")
        

        game_end()
    else

        local t1,t2 = math.modf(game_time / 1000)

        if (t2 < 0.1) then

            local string = "time: "..tostring(t1).."s"
            html_time:update_js("changeword(".."'"..string.."'"..")")

        end

    end
end

function update_score()
    score = score + 1
    local string = "score: "..tostring(score)
    html_score:update_js("changeword(".."'"..string.."'"..")")

    if (score == 27) then
        game_end()
    end

end

function game_end()

    ARLOG("game_end")
    gamestart = false
    scene.gameover:set_property_bool("visible",true)
    scene.button_shot:set_property_bool("visible",false)
    scene.aim:set_property_bool("visible",false)

    --从后往前删除
    for i = #all_cloned_balls, 1, -1 do
        if all_cloned_balls[i] ~= nil then

            local name = all_cloned_balls[i].entity:get_property_string("name")
            scene:remove_node_by_name(name)
            table.remove(all_cloned_balls, i)
        end
    end

    for i = #all_cloned_brick, 1, -1 do
        if all_cloned_brick[i] ~= nil then
            scene:remove_node_by_name(all_cloned_brick[i].brick_name)
            table.remove(all_cloned_brick, i)
        end
    end

end

scene.gameover.on_click = function()
    ARLOG("game restart")
    scene.gameover:set_property_bool("visible",false)
    diff_choose_button(true)
end

function ball_collision_handler(name_a, name_b, pos_a, pos_b, num)
    
    if (name_b ~= "floor") then
        ARLOG("play_box_collision_handler "..name_a.." "..name_b)

        local same_type = is_same_type(name_a, name_b)
        local same_color = is_same_color(name_a, name_b)

        if (same_type) then
            play_hit_ball_music()
        end

        if ((not same_color) and (not same_type)) then
            play_hit_brick_music()
        end

        if ( same_color and (not same_type)) then

            for i,v in pairs(all_cloned_brick) do
                if (v.brick_name == name_b) then
                    ARLOG("remove brick name "..v.brick_name)
                    table.remove(all_cloned_brick, i)
                end
            end

            scene:remove_node_by_name(name_b)
            play_brick_disappear_music()
            update_score()
        end
    end
end

local if_play_hit_ball_music = true
local if_play_hit_brick_music = true
local if_play_brick_disappear_music = true

function play_hit_ball_music()

    if (if_play_hit_ball_music) then
        local media_controller = root:get_media_controller()
        local config = {}
        config["repeat_count"] = 1
        config["delay"] = 0
        local media_session = media_controller:create_media_session("audio", "res/media/ball_hit_ball.mp3", config)
        media_session:play()
        ARLOG("play_hit_ball_music")

        if_play_hit_ball_music = false

        AR:perform_after(500, function()
            if_play_hit_ball_music = true
        end)
    end
end

function play_hit_brick_music()

    if (if_play_hit_brick_music) then
        local media_controller = root:get_media_controller()
        local config = {}
        config["repeat_count"] = 1
        config["delay"] = 0
        local media_session = media_controller:create_media_session("audio", "res/media/ball_hit_brick.mp3", config)
        media_session:play()
        ARLOG("play_hit_brick_music")

        if_play_hit_brick_music = false

        AR:perform_after(800, function()
            if_play_hit_brick_music = true
        end)

    end
end

function play_brick_disappear_music()

    if (if_play_brick_disappear_music) then
        local media_controller = root:get_media_controller()
        local config = {}
        config["repeat_count"] = 1
        config["delay"] = 0
        local media_session = media_controller:create_media_session("audio", "res/media/brick_disappear.mp3", config)
        media_session:play()
        ARLOG("play_brick_disappear_music")

        if_play_brick_disappear_music = false
        AR:perform_after(500, function()
            if_play_brick_disappear_music = true
        end)
    end
end




function is_same_type(name_a, name_b)
    local a_index =  string.find(name_a, ")")
    local b_index =  string.find(name_b, ")")

    if (a_index ~= nil and b_index ~= nil) then
        local sub_a = string.sub(name_a, a_index+1)
        local sub_b = string.sub(name_b, b_index+1)
        a_index = string.find(sub_a, "_")
        b_index = string.find(sub_b, "_")

        if (a_index ~= nil and b_index ~= nil) then
            sub_a = string.sub(sub_a, 1, a_index-1)
            sub_b = string.sub(sub_b, 1, b_index-1)

            --ARLOG("is_same_type "..sub_a.." "..sub_b)

            if (sub_a == sub_b) then
                 return true
            end
        end
    end
    return false

end

function is_same_color(ball_name, brick_name)

    local ball_index =  string.find(ball_name, "_")
    local brick_index =  string.find(brick_name, "_")

    if (ball_index ~= nil and brick_index ~= nil) then
        local ball_type = string.sub(ball_name, ball_index+1)
        local brick_type = string.sub(brick_name, brick_index+1)

        --ARLOG("is_same_color "..ball_type.." "..brick_type)

        if (ball_type == brick_type) then
             return true
        end
    end
    return false

end

scene.normal.on_click = function()
    
    gamediff = {_type = "kinematic", _mass = 0.5}
    start_game()
end

scene.hard.on_click = function()
    gamediff = {_type = "dynamic", _mass = 0.5}
    start_game()
end

scene.crazy.on_click = function()
    gamediff = {_type = "dynamic", _mass = 0.1}
    start_game()
end









scene.button_left.on_click = function()


end

scene.button_right.on_click = function()


end

scene.button_down.on_click = function()



end

scene.button_up.on_click = function()

end

scene.button_stop.on_click = function()


end

scene.button_a.on_click = function()


end

scene.button_b.on_click = function()


end



scene.button_c.on_click = function()



end

scene.button_d.on_click = function()

local physics_body = scene.ball3:get_physics_body()

local force = physics_body:get_property_vec3("force")
local torque = physics_body:get_property_vec3("torque")
local linear_velocity = physics_body:get_property_vec3("linear_velocity")
local angular_velocity = physics_body:get_property_vec3("angular_velocity")
local linear_factor = physics_body:get_property_vec3("linear_factor")
local angular_factor = physics_body:get_property_vec3("angular_factor")
local gravity = physics_body:get_property_vec3("gravity")
local world_transform = physics_body:get_property_mat44("world_transform")

ARLOG("force "..force:to_string())
ARLOG("torque "..torque:to_string())
ARLOG("linear_velocity "..linear_velocity:to_string())
ARLOG("angular_velocity "..angular_velocity:to_string())
ARLOG("linear_factor "..linear_factor:to_string())
ARLOG("angular_factor "..angular_factor:to_string())
ARLOG("gravity "..gravity:to_string())
ARLOG("world_transform "..world_transform:to_string())


end



scene.ResumeButton100.on_click = function()


end

