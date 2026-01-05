document.addEventListener('DOMContentLoaded', async () => {
    const profileContainer = document.getElementById('profile-content');
    const loadingState = document.getElementById('loading-state');

    // 로그아웃 처리
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', async (e) => {
            e.preventDefault();
            try {
                await window.api.logout();
                window.location.href = '/login.html';
            } catch (error) {
                window.location.href = '/login.html';
            }
        });
    }

    try {
        const response = await window.api.getMyProfile();
        const user = response.data;

        loadingState.style.display = 'none';
        
        profileContainer.innerHTML = `
            <div class="profile-card">
                <div class="profile-header">
                    <div class="profile-avatar">
                        ${user.nickname ? user.nickname.charAt(0).toUpperCase() : 'U'}
                    </div>
                    <div>
                        <h2 style="margin: 0;">${user.nickname}</h2>
                        <span style="color: #64748b;">${user.email}</span>
                    </div>
                </div>
                
                <div class="profile-info-group">
                    <label class="profile-label">이메일</label>
                    <div class="profile-value">${user.email}</div>
                </div>
                
                <div class="profile-info-group">
                    <label class="profile-label">닉네임</label>
                    <div class="profile-value">${user.nickname}</div>
                </div>

                <div class="profile-info-group">
                    <label class="profile-label">가입일</label>
                    <div class="profile-value">${new Date().toLocaleDateString()} (Mock)</div>
                </div>
            </div>
        `;

    } catch (error) {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            window.location.href = '/login.html';
            return;
        }
        loadingState.innerHTML = `
            <div class="error">
                프로필을 불러올 수 없습니다.<br>
                ${error.message}
            </div>
        `;
    }
});
