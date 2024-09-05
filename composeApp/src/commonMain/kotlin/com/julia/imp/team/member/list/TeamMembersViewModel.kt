package com.julia.imp.team.member.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.team.member.Role
import com.julia.imp.team.member.TeamMember
import com.julia.imp.team.member.TeamMemberRepository
import kotlinx.coroutines.launch

class TeamMembersViewModel(
    private val repository: TeamMemberRepository = TeamMemberRepository()
) : ViewModel() {
    
    var uiState by mutableStateOf(TeamMembersUiState())
        private set

    fun getMembers() {
        viewModelScope.launch {
            try {
                val isAdmin = requireSession().isTeamAdmin

                uiState = TeamMembersUiState(
                    loading = true,
                    showAddButton = isAdmin,
                    showOptions = isAdmin
                )

                val members = repository.getMembers(requireTeam().id)

                uiState = uiState.copy(members = members)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun askToAdd() {
        uiState = uiState.copy(showAddDialog = true)
    }

    fun dismissAdding() {
        uiState = uiState.copy(showAddDialog = false)
    }

    fun add(email: String, role: Role) {
        viewModelScope.launch {
            try {
                repository.addMember(requireTeam().id, email, role)
                getMembers()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun askToRemove(member: TeamMember) {
        uiState = uiState.copy(memberToRemove = member)
    }

    fun dismissRemoval() {
        uiState = uiState.copy(memberToRemove = null)
    }

    fun remove(member: TeamMember) {
        viewModelScope.launch {
            try {
                repository.removeMember(member.teamId, member.userId)
                getMembers()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun askToChangeRole(member: TeamMember) {
        uiState = uiState.copy(memberToChangeRole = member)
    }

    fun dismissRoleChange() {
        uiState = uiState.copy(memberToChangeRole = null)
    }

    fun askToUpdatetHourlyCostDialog(member: TeamMember) {
        uiState = uiState.copy(memberToUpdateHourlyCost = member)
    }

    fun dismissHourlyCostDialog() {
        uiState = uiState.copy(memberToUpdateHourlyCost = null)
    }

    fun changeRole(member: TeamMember, newRole: Role) {
        viewModelScope.launch {
            try {
                repository.updateMember(member.teamId, member.userId, newRole, member.hourlyCost)
                getMembers()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun updateHourlyCost(member: TeamMember, newHourlyCost: Double) {
        viewModelScope.launch {
            try {
                repository.updateMember(member.teamId, member.userId, member.role, newHourlyCost)
                getMembers()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}