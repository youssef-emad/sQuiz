package com.example.httpRequest;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

import com.example.Models.Group;
import com.google.gson.JsonObject;

public interface GroupApi {
	@Headers({"Accept: application/json"})
	@GET("/{type}/groups") 
	public void instructorRequestGroups(@Header("X-Instructor-Email") String email,@Header("X-Instructor-Token") String token ,@Path("type") String type,Callback<List<Group>> callback);		
	
	
	@Headers({"Accept: application/json"})
	@GET("/{type}/groups") //request groups list for  student
	public void studentRequestGroups(@Header("X-Student-Email") String email,@Header("X-Student-Token") String token ,@Path("type") String type,Callback<List<Group>> callback);	

	
	@Headers({"Accept: application/json"})
	@POST("/groups/create")
	public void addGroup(@Header("X-Instructor-Email") String email,@Header("X-Instructor-Token") String token,@Body  Group group,Callback<Group> callback);
	
	@Headers({"Accept: application/json"})
	@POST("/groups/delete")
	public void deleteGroups(@Header("X-Instructor-Email") String email,@Header("X-Instructor-Token") String token,@Body List<Group> groups,Callback<JsonObject> callback);
} 
