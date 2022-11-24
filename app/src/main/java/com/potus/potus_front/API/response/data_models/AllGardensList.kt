package com.potus.potus_front.API.response.data_models

import com.potus.potus_front.API.response.NewGardenResponse

data class AllGardensList(
    var gardens: List<NewGardenResponse>
)