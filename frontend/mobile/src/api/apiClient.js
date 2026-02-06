import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'https://concerned-minni-meteorfish-04bbceb2.koyeb.app',
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true // Important for session-based auth
});

// Response interceptor for error handling
apiClient.interceptors.response.use(
    response => response,
    error => {
        console.error('API Error:', error);
        return Promise.reject(error);
    }
);

export default apiClient;
