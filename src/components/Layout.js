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
          <Link to="/" className="logo">Система Очередей</Link>
          <nav className="nav-links">
            {isAuthenticated ? (
              <>
                <Link to="/queues">Очереди</Link>
                <Link to="/profile">Профиль</Link>
                {user && user.role === 'ADMIN' && (
                  <Link to="/admin">Панель Администратора</Link>
                )}
                <button onClick={handleLogout} className="btn btn-secondary">Выход</button>
              </>
            ) : (
              <>
                <Link to="/login">Вход</Link>
                <Link to="/register">Регистрация</Link>
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
          <p>&copy; {new Date().getFullYear()} Система Очередей. Все права защищены.</p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;
