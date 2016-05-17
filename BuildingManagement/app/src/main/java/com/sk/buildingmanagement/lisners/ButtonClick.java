package com.sk.buildingmanagement.lisners;

/**
 * Created by ISKM on 05/12/2016.
 */
public interface ButtonClick
{
    public void onButtonClick(String building_id,String wing_id,String add_floor_name,String add_floor_total_flat,String add_floor_series_start,String add_floor_series_end,String add_floor_total_sq_feet);
    public void update_onButtonClick(Integer old_series_end,String floor_id,String building_id,String wing_id,String add_floor_name,String add_floor_total_flat,String add_floor_series_start,String add_floor_series_end,String add_floor_total_sq_feet);

}
