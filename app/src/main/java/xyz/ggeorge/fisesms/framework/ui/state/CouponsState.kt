package xyz.ggeorge.fisesms.framework.ui.state

import xyz.ggeorge.fisesms.data.entities.FiseEntity

data class CouponsState(
    val coupons: List<FiseEntity> = emptyList(),
    val sortType: SortType = SortType.PROCESS_TIMESTAMP_DESC
)
