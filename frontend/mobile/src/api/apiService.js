import apiClient from './apiClient';

const apiService = {
    // Auth
    logout: () => apiClient.post('/api/auth/logout'),

    // Companies
    getCompanies: () => apiClient.get('/api/companies'),
    getCompany: (id) => apiClient.get(`/api/companies/${id}`),
    createCompany: (data) => apiClient.post('/api/companies', data),

    // Reviews
    getReviews: () => apiClient.get('/api/reviews'),
    createReview: (data) => apiClient.post('/api/reviews', data),
    getReview: (id) => apiClient.get(`/api/reviews/${id}`),

    // Members
    getMyProfile: () => apiClient.get('/api/members/me'),
    updateNickname: (data) => apiClient.patch('/api/members/me/nickname', data),
};

export default apiService;
