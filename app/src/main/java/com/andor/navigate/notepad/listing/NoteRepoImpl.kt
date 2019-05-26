package com.andor.navigate.notepad.listing

class NoteRepoImpl : NoteRepo {
    override fun getNotes(): HashMap<Int, ListModel> {
        return hashMapOf(
            0 to ListModel("Head1", "body1"),
            1 to ListModel("Head2", "body2"),
            2 to ListModel("Head3", "body3"),
            3 to ListModel("Head4", "body4"),
            4 to ListModel("Head5", "body5")
        )
    }

}
