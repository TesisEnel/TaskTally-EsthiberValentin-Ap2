package edu.ucne.tasktally.domain.usecases

import edu.ucne.tasktally.domain.models.UserInfo
import edu.ucne.tasktally.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repo: UserInfoRepository
) {
    suspend operator fun invoke(id: Int?): UserInfo? = repo.getUserInfo(id)
}
