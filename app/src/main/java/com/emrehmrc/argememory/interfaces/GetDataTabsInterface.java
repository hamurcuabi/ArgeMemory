package com.emrehmrc.argememory.interfaces;

import com.emrehmrc.argememory.model.DepartmentModel;

import java.util.ArrayList;

public interface GetDataTabsInterface {

    public void getDeps(ArrayList<DepartmentModel> departmentModels);
    public void getPers();
    public void getTags();
}
