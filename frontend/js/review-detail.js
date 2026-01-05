document.addEventListener('DOMContentLoaded', async () => {
    const container = document.getElementById('review-detail-content');
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

    // URL 파라미터에서 ID 추출
    const urlParams = new URLSearchParams(window.location.search);
    const reviewId = urlParams.get('id');

    if (!reviewId) {
        alert('잘못된 접근입니다.');
        window.location.href = '/reviews.html';
        return;
    }

    try {
        const response = await window.api.getReview(reviewId);
        const review = response.data;

        loadingState.style.display = 'none';

        const stars = '★'.repeat(review.rating) + '☆'.repeat(5 - review.rating);
        const date = new Date(review.createdAt).toLocaleDateString();

        container.innerHTML = `
            <a href="/reviews.html" class="btn-back">← 목록으로 돌아가기</a>
            
            <div class="review-detail-container">
                <div class="review-header">
                    <h1 class="review-title">${review.title}</h1>
                    <div class="review-meta">
                        <span>작성일: ${date}</span>
                        <span class="review-rating">${stars} (${review.rating}점)</span>
                    </div>
                </div>

                <div class="review-section">
                    <label class="section-label">상세 내용</label>
                    <div class="review-content">${review.content}</div>
                </div>

                ${review.tips ? `
                <div class="review-tips">
                    <label class="section-label" style="color: #1e40af;">💡 꿀팁</label>
                    <div class="tips-content">${review.tips}</div>
                </div>
                ` : ''}
            </div>
        `;

    } catch (error) {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            window.location.href = '/login.html';
            return;
        }
        loadingState.innerHTML = `
            <div class="error">
                리뷰를 불러올 수 없습니다.<br>
                ${error.message}
            </div>
            <br>
            <a href="/reviews.html" class="btn">목록으로 돌아가기</a>
        `;
    }
});
