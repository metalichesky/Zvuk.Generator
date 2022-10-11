package com.metalichesky.zvuk.generator.model

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlphabetInteractor @Inject constructor(
    private val alphabetRepo: AlphabetRepo
) {

    val defaultAlphabet: Flow<Alphabet?> = alphabetRepo.defaultAlphabet

    suspend fun setDefaultAlphabet(id: Long) {
        alphabetRepo.setDefaultAlphabet(id)
    }

    suspend fun getDefaultAlphabet(): Alphabet? {
        return alphabetRepo.getDefaultAlphabet()
    }

    suspend fun getAllAlphabets(): List<Alphabet> {
        return alphabetRepo.getAllAlphabets()
    }

}