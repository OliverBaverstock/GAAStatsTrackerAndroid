package oliver.gaa_stats_tracker.models

//Creates a class of type Match and initializes the attributes of Match
data class Match(var oppGoals: Int, var oppName: String, var oppPoints: Int, var teamGoals: Int, var teamName: String, var teamPoints: Int, var userID: String){

    constructor() : this(0, "", 0, 0, "", 0, "")

}



