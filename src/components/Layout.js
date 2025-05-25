import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { logout } from '../store/slices/authSlice';

const Layout = () => {
  const { isAuthenticated, user } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  return (
    <div className="app-container">
      <header className="header">
        <div className="container header-container">
          <Link to="/" className="logo">Queue System</Link>
          <nav className="nav-links">
            {isAuthenticated ? (
              <>
                <Link to="/queues">Queues</Link>
                <Link to="/profile">Profile</Link>
                <button onClick={handleLogout} className="btn btn-secondary">Logout</button>
              </>
            ) : (
              <>
                <Link to="/login">Login</Link>
                <Link to="/register">Register</Link>
              </>
            )}
          </nav>
        </div>
      </header>
      
      <main className="main-content">
        <div className="container">
          <Outlet />
        </div>
      </main>
      
      <footer className="footer">
        <div className="container text-center">
          <p>&copy; {new Date().getFullYear()} Queue System. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;