package edu.ucne.tasktally.domain.usecases.userinfo

import edu.ucne.tasktally.domain.models.UserInfo
import edu.ucne.tasktally.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfosByRoleUseCase @Inject constructor(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(role: String): List<UserInfo> =
        repository.getUsersByRole(role)
}
