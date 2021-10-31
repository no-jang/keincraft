#version 330 core

in vec2 passTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
	fragColor = texture(texture_sampler, passTexCoord);
}