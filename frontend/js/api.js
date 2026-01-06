const apiClient = axios.create({
    baseURL: 'https://concerned-minni-meteorfish-04bbceb2.koyeb.app',
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true
});

// Response interceptor for error handling (optional but good practice)
apiClient.interceptors.response.use(
    response => response,
    error => {
        console.error('API Error:', error);
        return Promise.reject(error);
    }
);

const api = {
    // Auth
    logout: () => apiClient.post('/api/auth/logout'),

    // Companies
    getCompanies: () => apiClient.get('/api/companies'),
    getCompany: (id) => apiClient.get(`/api/companies/${id}`),
    createCompany: (data) => apiClient.post('/api/companies', data),

    // Reviews
    getReviews: () => apiClient.get('/api/reviews'), // 리뷰 목록 조회 추가
    createReview: (data) => apiClient.post('/api/reviews', data),
    getReview: (id) => apiClient.get(`/api/reviews/${id}`),

    // Members
    getMyProfile: () => apiClient.get('/api/members/me'),
    updateNickname: (data) => apiClient.patch('/api/members/me/nickname', data),
};

// Expose to window for simple usage in app.js
window.api = api;
