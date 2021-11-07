#version 330 core

layout (location = 0) in vec3 inVertexCoord;
layout (location = 1) in vec2 inTextureCoord;

out vec3 passTextureCoord;

uniform mat4 projViewMatrix;

void main()
{
	gl_Position = projViewMatrix * vec4(inVertexCoord, 1.0f);
	passTextureCoord = inTextureCoord;
}