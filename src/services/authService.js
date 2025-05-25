import api from './api';

const authService = {
  register: async (userData) => {
    const response = await api.post('/register', userData);
    if (response.data) {
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('refreshToken', response.data.refreshToken);
    }
    return response.data;
  },

  login: async (credentials) => {
    const response = await api.post('/login', credentials);
    if (response.data) {
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('refreshToken', response.data.refreshToken);
    }
    return response.data;
  },

  logout: async () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    return { success: true };
  },

  refreshToken: async () => {
    const response = await api.post('/refresh_token');
    if (response.data) {
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('refreshToken', response.data.refreshToken);
    }
    return response.data;
  },

  getCurrentUser: () => {
    return JSON.parse(localStorage.getItem('user'));
  },

  isAuthenticated: () => {
    const user = JSON.parse(localStorage.getItem('user'));
    return !!user;
  },
};

export default authService;