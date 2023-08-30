package one.njk.celestidesk.domain

data class NewBreakRequest(
    val subject: String,
    val message: String,
    var emergency: Boolean,
    val from: String,
    val to: String,
)

// TODO: Test sending date with long, then LocalDateTime, then finally String
