package com.dashen.ningbaoqi.factory.model.db;

import com.dashen.ningbaoqi.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 基础的BaseDbModel
 */
public abstract class BaseDbModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model> {

}
