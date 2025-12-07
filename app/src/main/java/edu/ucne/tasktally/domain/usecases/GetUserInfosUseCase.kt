package edu.ucne.tasktally.domain.usecases

import edu.ucne.tasktally.domain.models.UserInfo
import edu.ucne.tasktally.domain.repository.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfosUseCase @Inject constructor(
    private val repo: UserInfoRepository
) {
    operator fun invoke(): Flow<List<UserInfo>> = repo.observeUserInfos()
}
