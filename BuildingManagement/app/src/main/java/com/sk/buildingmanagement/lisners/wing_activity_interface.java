package com.sk.buildingmanagement.lisners;

/**
 * Created by ISKM on 05/12/2016.
 */
public interface wing_activity_interface
{
    public void delete_wing_call_back(String building_id,String wing_id);
    public void edit_wing_call_back(String building_id,String wing_id,String wingname,String total_floor,String ground_floor,String basement);
}
