document.addEventListener('DOMContentLoaded', async () => {
    const listContainer = document.getElementById('review-list');
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
        // 리뷰 목록 API 호출
        const response = await window.api.getReviews();
        const reviews = response.data;

        loadingState.style.display = 'none';

        if (reviews.length === 0) {
            listContainer.innerHTML = '<p>등록된 후기가 없습니다.</p>';
            return;
        }

        let html = '';
        reviews.forEach(review => {
            // 평점을 별(★)로 표시
            const stars = '★'.repeat(review.rating) + '☆'.repeat(5 - review.rating);
            
            html += `
                <div class="card">
                    <div style="display: flex; justify-content: space-between; align-items: flex-start;">
                        <h3 style="margin-top: 0;">${review.title}</h3>
                        <span style="color: #f59e0b; font-size: 1.2rem;">${stars}</span>
                    </div>
                    <p style="color: #64748b; font-size: 0.9rem; margin-bottom: 1rem;">
                        ${new Date(review.createdAt).toLocaleDateString()}
                    </p>
                    <p style="margin-bottom: 1rem; line-height: 1.5;">
                        ${review.content.length > 100 ? review.content.substring(0, 100) + '...' : review.content}
                    </p>
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <span style="background: #f1f5f9; padding: 0.25rem 0.5rem; border-radius: 4px; font-size: 0.8rem; color: #475569;">
                            꿀팁 포함
                        </span>
                        <button class="btn" style="padding: 0.5rem 1rem; font-size: 0.875rem;" 
                            onclick="alert('Review Detail for ID: ${review.id} (Not implemented fully)')">상세 보기</button>
                    </div>
                </div>
            `;
        });
        listContainer.innerHTML = html;

    } catch (error) {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            window.location.href = '/login.html';
            return;
        }
        loadingState.innerHTML = `<div class="error">Failed to load reviews: ${error.message}</div>`;
    }
});
