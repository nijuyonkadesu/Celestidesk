package one.njk.celestidesk.data

data class BreakRequest(
    val subject: String,
    val message: String,
    val date: String = "2023-01-03"
)
// TODO: Use Date type for requested date
// TODO: Network, db, UI should have separate BreakRequest dataclass
