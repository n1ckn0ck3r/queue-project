import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import QueueListPage from './pages/QueueListPage';
import QueueDetailPage from './pages/QueueDetailPage';
import ProfilePage from './pages/ProfilePage';
import NotFoundPage from './pages/NotFoundPage';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import './styles/App.css';

function App() {
  const { isAuthenticated } = useSelector((state) => state.auth);

  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        {/* Public routes */}
        <Route index element={isAuthenticated ? <Navigate to="/queues" /> : <LoginPage />} />
        <Route path="login" element={isAuthenticated ? <Navigate to="/queues" /> : <LoginPage />} />
        <Route path="register" element={isAuthenticated ? <Navigate to="/queues" /> : <RegisterPage />} />
        
        {/* Protected routes */}
        <Route element={<ProtectedRoute isAuthenticated={isAuthenticated} />}>
          <Route path="queues" element={<QueueListPage />} />
          <Route path="queues/:id" element={<QueueDetailPage />} />
          <Route path="profile" element={<ProfilePage />} />
        </Route>
        
        {/* 404 route */}
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}

export default App;