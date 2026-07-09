package com.devflow.app.features.monitoring.data.mapper

import com.devflow.app.features.monitoring.data.local.entity.RepositoryConfigEntity
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig

fun RepositoryConfigEntity.toDomain(): RepositoryConfig {
    return RepositoryConfig(
        id = id,
        owner = owner,
        repository = repository,
        personalAccessToken = personalAccessToken,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun RepositoryConfig.toEntity(): RepositoryConfigEntity {
    return RepositoryConfigEntity(
        id = id,
        owner = owner,
        repository = repository,
        personalAccessToken = personalAccessToken,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
