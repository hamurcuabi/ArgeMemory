package com.emrehmrc.argememory.interfaces;

import com.emrehmrc.argememory.model.DepartmentModel;
import com.emrehmrc.argememory.model.PersonelModel;

import java.util.ArrayList;

public interface ShareInterface {
    void dialogDep(ArrayList<DepartmentModel> depList);
    void dialogPers(ArrayList<PersonelModel> persList);
}
