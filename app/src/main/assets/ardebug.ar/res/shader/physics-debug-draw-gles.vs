attribute vec3 position;
attribute vec4 color;
attribute vec3 normal;
attribute vec2 texcoord;
uniform mat4 View;
uniform mat4 Proj;
uniform mat4 World;
varying vec4 acolor;
void main() {
    gl_Position = Proj * View * World * vec4(position, 1.0);
    acolor = color;
}
