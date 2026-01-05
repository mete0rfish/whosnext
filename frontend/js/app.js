document.addEventListener('DOMContentLoaded', () => {
    const app = document.getElementById('main-content');

    // Simple Router
    const routes = {
        'home': renderHome,
        'companies': renderCompanyList,
        'reviews': renderReviewCreate, // Temporary mapping since list is missing
        'profile': renderProfile,
        'create-company': renderCompanyCreate
    };

    function navigate(view) {
        if (routes[view]) {
            routes[view]();
        } else {
            renderHome();
        }
    }

    // Navigation setup
    document.querySelectorAll('[data-link]').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const view = e.target.getAttribute('data-link');
            navigate(view);
        });
    });

    // Logout setup
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', async (e) => {
            e.preventDefault();
            try {
                await window.api.logout();
                window.location.href = '/login.html';
            } catch (error) {
                console.error('Logout failed:', error);
                // 에러가 나더라도 일단 로그인 페이지로 이동시키는 것이 안전함
                window.location.href = '/login.html';
            }
        });
    }

    // Initial load
    checkLoginStatus().then(() => {
        navigate('home');
    });

    // --- Auth Check ---
    async function checkLoginStatus() {
        try {
            // 프로필 정보를 요청하여 로그인 여부 확인
            await window.api.getMyProfile();
        } catch (error) {
            // 401 Unauthorized 또는 403 Forbidden 에러 발생 시 로그인 페이지로 이동
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                window.location.href = '/login.html';
            }
        }
    }

    // --- Views ---

    function renderHome() {
        app.innerHTML = `
            <section class="hero" style="text-align: center; padding: 4rem 0;">
                <h1 style="font-size: 3rem; margin-bottom: 1rem; background: linear-gradient(135deg, #3b82f6, #8b5cf6); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
                    당신의 다음 커리어는 어디인가요?
                </h1>
                <p style="font-size: 1.25rem; color: #94a3b8; margin-bottom: 2rem;">
                    졸업생들의 취업 현황을 공유하고 함께 성장하세요.
                </p>
                <a href="#" class="btn" onclick="document.querySelector('[data-link=companies]').click()">후기 보러가기</a>
            </section>
        `;
    }

    async function renderCompanyList() {
        app.innerHTML = '<div class="loading-state">Loading companies...</div>';
        try {
            const response = await window.api.getCompanies();
            const companies = response.data;

            let html = `
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h2>취업 후기</h2>
                    <button class="btn" id="btn-create-company">새 기업 등록</button>
                </div>
                <div class="grid-list">
            `;

            if (companies.length === 0) {
                html += '<p>등록된 기업이 없습니다.</p>';
            } else {
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
            }
            html += '</div>';
            app.innerHTML = html;

            document.getElementById('btn-create-company').addEventListener('click', () => navigate('create-company'));

        } catch (error) {
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                window.location.href = '/login.html';
                return;
            }
            app.innerHTML = `<div class="error">Failed to load companies: ${error.message}</div>`;
        }
    }

    function renderCompanyCreate() {
        app.innerHTML = `
            <h2>새 기업 등록</h2>
            <form id="create-company-form" style="max-width: 500px; margin-top: 2rem;">
                <div class="form-group">
                    <label>기업명</label>
                    <input type="text" name="name" required>
                </div>
                <div class="form-group">
                    <label>산업군</label>
                    <input type="text" name="industry" required>
                </div>
                <div class="form-group">
                    <label>위치</label>
                    <input type="text" name="location" required>
                </div>
                <button type="submit" class="btn">등록하기</button>
            </form>
        `;

        document.getElementById('create-company-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const data = {
                name: formData.get('name'),
                industry: formData.get('industry'),
                location: formData.get('location')
            };

            try {
                await window.api.createCompany(data);
                alert('기업이 등록되었습니다!');
                navigate('companies');
            } catch (error) {
                if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                    window.location.href = '/login.html';
                    return;
                }
                alert('등록 실패: ' + error.message);
            }
        });
    }

    function renderReviewCreate() {
        app.innerHTML = `
            <h2>후기 작성 (Demo Step)</h2>
            <p>후기를 작성하려면 먼저 기업을 선택해야 합니다. (This flow needs refinement based on specific company selection)</p>
            <!-- Basic form for demo -->
            <form id="create-review-form" style="max-width: 500px; margin-top: 2rem;">
                <div class="form-group">
                    <label>Company ID (UUID)</label>
                    <input type="text" name="companyId" placeholder="Enter Company UUID" required>
                </div>
                 <div class="form-group">
                    <label>제목</label>
                    <input type="text" name="title" required>
                </div>
                <div class="form-group">
                    <label>내용</label>
                    <textarea name="content" rows="4" required></textarea>
                </div>
                 <div class="form-group">
                    <label>팁</label>
                    <textarea name="tips" rows="2"></textarea>
                </div>
                 <div class="form-group">
                    <label>평점 (1-5)</label>
                    <input type="number" name="rating" min="1" max="5" required>
                </div>
                <button type="submit" class="btn">후기 등록</button>
            </form>
        `;
        document.getElementById('create-review-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const data = {
                companyId: formData.get('companyId'),
                title: formData.get('title'),
                content: formData.get('content'),
                tips: formData.get('tips'),
                rating: parseInt(formData.get('rating'))
            };

            try {
                await window.api.createReview(data);
                alert('후기가 등록되었습니다!');
                navigate('home');
            } catch (error) {
                if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                    window.location.href = '/login.html';
                    return;
                }
                alert('등록 실패: ' + error.message);
            }
        });
    }

    async function renderProfile() {
        app.innerHTML = 'Loading profile...';
        try {
            const response = await window.api.getMyProfile();
            const user = response.data;
            app.innerHTML = `
                <h2>내 프로필</h2>
                <div class="card" style="max-width: 500px; margin-top: 2rem;">
                    <p><strong>이메일:</strong> ${user.email}</p>
                    <p><strong>닉네임:</strong> ${user.nickname}</p>
                    <p><strong>가입일:</strong> ${new Date().toLocaleDateString()} (Mock)</p> 
                </div>
            `;
        } catch (error) {
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                window.location.href = '/login.html';
                return;
            }
            app.innerHTML = `
                <div class="error">
                    로그인이 필요하거나 프로필을 불러올 수 없습니다.<br>
                    ${error.message}
                </div>
            `;
        }
    }
});
