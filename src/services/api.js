import axios from 'axios';

const hostname = window.location.hostname;

let baseURL;
if (hostname === 'localhost' || hostname === '127.0.0.1') {
  baseURL = 'http://localhost:8080';
} else {
  // здесь hostname будет равен '25.100.200.50'
  baseURL = `http://${hostname}:8080`;
}

const api = axios.create({
  // baseURL: 'http://localhost:8080/',
  baseURL: baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to add the auth token to requests
api.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle token refresh
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    
    // If the error is 401 and we haven't already tried to refresh the token
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        // Try to refresh the token      
        const { data } = await axios.post('/refresh_token', {}, { withCredentials: true });
        localStorage.setItem('accessToken', data.accessToken);
        
        // Update the Authorization header
        originalRequest.headers.Authorization = `Bearer ${data.accessToken}`;
        
        // Retry the original request
        return api(originalRequest);
      } catch (refreshError) {
        // If refresh fails, redirect to login
        localStorage.removeItem('user');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);

export default api;