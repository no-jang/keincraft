#version 330 core

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 inTexCoord;

out vec2 passTexCoord;

uniform mat4 projViewModelMatrix;

void main()
{
	gl_Position = projViewModelMatrix * vec4(inPosition, 1.0f);
	passTexCoord = inTexCoord;
}