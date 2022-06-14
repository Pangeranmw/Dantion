package com.bangkit.dantion.data.remote.distanceMatrix

data class GetMatrixResponse(
    val destination_addresses: List<String>,
    val origin_addresses: List<String>,
    val rows: List<Rows>,
    val status: String,
    val error_message: String,
)
data class Rows(
    val elements: List<Elements>
)
data class Elements(
    val distance: Distance,
    val duration: Duration,
    val duration_in_traffic: Duration? = null,
    val status: String,
)
data class Distance(
    val text: String,
    val value: String,
)
data class Duration(
    val text: String,
    val value: String,
)