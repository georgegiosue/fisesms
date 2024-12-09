package xyz.ggeorge.fisesms.framework.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import xyz.ggeorge.fisesms.data.dao.FiseDao
import xyz.ggeorge.fisesms.data.entities.FiseEntity


class ChartsViewModel(val fiseDao: FiseDao) : ViewModel() {

    fun getFiseData(): Flow<List<FiseEntity>> {
        return fiseDao.getAllOrderedByProcessedTimestampDesc()
    }
}