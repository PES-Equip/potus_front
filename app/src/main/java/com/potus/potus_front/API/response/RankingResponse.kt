package com.potus.potus_front.API.response

import com.potus.potus_front.API.response.data_models.Ranking
import com.potus.potus_front.API.response.data_models.Trophy

data class RankingResponse(
    var trophy: Trophy,
    var classification: List<Ranking>
)