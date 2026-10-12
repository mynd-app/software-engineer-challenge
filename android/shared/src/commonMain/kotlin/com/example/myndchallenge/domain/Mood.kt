package com.example.myndchallenge.domain

/** The fixed list of moods a user can check in with. [apiValue] is what the backend stores. */
enum class Mood(
    val apiValue: String,
    val label: String,
) {
    HAPPY("happy", "Happy"),
    CALM("calm", "Calm"),
    GRATEFUL("grateful", "Grateful"),
    ENERGISED("energised", "Energised"),
    TIRED("tired", "Tired"),
    ANXIOUS("anxious", "Anxious"),
    STRESSED("stressed", "Stressed"),
    SAD("sad", "Sad"),
    ANGRY("angry", "Angry"),
    LONELY("lonely", "Lonely"),
    ;

    companion object {
        fun fromApi(value: String): Mood? = entries.firstOrNull { it.apiValue == value }
    }
}
