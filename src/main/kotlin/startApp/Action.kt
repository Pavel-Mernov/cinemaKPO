package startApp

// data class which represents name of action and the action itself
data class Action(val actionName : String, val action : () -> Unit)
{
    override fun toString(): String {
        return actionName
    }
}