package com.metalichesky.zvuk.generator.model

import com.metalichesky.zvuk.generator.model.mapper.AlphabetMapper
import com.metalichesky.zvuk.storage.AlphabetDataSource
import com.metalichesky.zvuk.storage.UserPrefsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlphabetRepo @Inject constructor(
    private val alphabetDataSource: AlphabetDataSource,
    private val userPrefsDataSource: UserPrefsDataSource
) {

    val defaultAlphabet: Flow<Alphabet?> = userPrefsDataSource.defaultAlphabetId.map {
        getAlphabetWithId(it)
    }

    suspend fun getAllAlphabets(): List<Alphabet> {
        return AlphabetMapper.map(alphabetDataSource.getAllAlphabets())
    }

    suspend fun getAlphabetWithType(type: AlphabetType): Alphabet? {
        return alphabetDataSource.getAlphabetWithType(type.name)?.let(AlphabetMapper::map)
    }

    suspend fun getAlphabetWithId(id: Long): Alphabet? {
        return alphabetDataSource.getAlphabetWithId(id)?.let(AlphabetMapper::map)
    }

    suspend fun getDefaultAlphabet(): Alphabet? {
        val defaultId = userPrefsDataSource.getDefaultAlphabetId()
        return alphabetDataSource.getAlphabetWithId(defaultId)?.let(AlphabetMapper::map)
    }

    suspend fun setDefaultAlphabet(id: Long) {
        userPrefsDataSource.setDefaultAlphabetId(id)
    }

}

