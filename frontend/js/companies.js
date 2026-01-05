document.addEventListener('DOMContentLoaded', async () => {
    const listContainer = document.getElementById('company-list');
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
        // API 호출
        const response = await window.api.getCompanies();
        const companies = response.data;

        loadingState.style.display = 'none';

        if (companies.length === 0) {
            listContainer.innerHTML = '<p>등록된 기업이 없습니다.</p>';
            return;
        }

        let html = '';
        companies.forEach(company => {
            html += `
                <div class="card">
                    <h3>${company.name}</h3>
                    <p style="color: #94a3b8; margin: 0.5rem 0;">${company.industry} | ${company.location}</p>
                    <button class="btn" style="padding: 0.5rem 1rem; font-size: 0.875rem; margin-top: 1rem;" 
                        onclick="alert('Company Detail for ID: ${company.id} (Not implemented fully)')">상세 보기</button>
                </div>
            `;
        });
        listContainer.innerHTML = html;

    } catch (error) {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            window.location.href = '/login.html';
            return;
        }
        loadingState.innerHTML = `<div class="error">Failed to load companies: ${error.message}</div>`;
    }
});
