package com.andor.navigate.notepad.listing

object NoteRepoImpl : NoteRepo {
    private val noteHashMap: HashMap<Int, ListModel> = HashMap()

    override fun addNote(listModel: ListModel) {
        noteHashMap[noteHashMap.size] = listModel
    }

    override fun deleteNote(indexKey: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNote(indexKey: Int, listModel: ListModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNotes(): HashMap<Int, ListModel> {
        return noteHashMap
    }

}
