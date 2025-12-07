package edu.ucne.tasktally.domain.usecases

import edu.ucne.tasktally.domain.models.UserInfo
import edu.ucne.tasktally.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfoByUserIdUseCase @Inject constructor(
    private val repo: UserInfoRepository
) {
    suspend operator fun invoke(userId: Int): UserInfo? = repo.getUserInfoByUserId(userId)
}
