import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { login, resetAuthState } from '../store/slices/authSlice';

const LoginPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });
  const [formErrors, setFormErrors] = useState({});

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isLoading, error, isAuthenticated } = useSelector((state) => state.auth);

  useEffect(() => {
    // Reset auth state when component mounts
    dispatch(resetAuthState());
  }, [dispatch]);

  useEffect(() => {
    // Redirect if authenticated
    if (isAuthenticated) {
      navigate('/queues');
    }
  }, [isAuthenticated, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const validateForm = () => {
    const errors = {};

    if (!formData.username) {
      errors.username = 'Имя пользователя обязательно';
    }

    if (!formData.password) {
      errors.password = 'Пароль обязателен';
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (validateForm()) {
      dispatch(login(formData));
    }
  };

  return (
    <div className="form-container">
      <h2 className="form-title">Вход в аккаунт</h2>

      {error && (
        <div className="alert alert-error">
          {error.message || 'Не удалось войти. Пожалуйста, попробуйте снова.'}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username" className="form-label">Имя пользователя</label>
          <input
            type="text"
            id="username"
            name="username"
            className="form-input"
            value={formData.username}
            onChange={handleChange}
          />
          {formErrors.username && <div className="form-error">{formErrors.username}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="password" className="form-label">Пароль</label>
          <input
            type="password"
            id="password"
            name="password"
            className="form-input"
            value={formData.password}
            onChange={handleChange}
          />
          {formErrors.password && <div className="form-error">{formErrors.password}</div>}
        </div>

        <button type="submit" className="form-button" disabled={isLoading}>
          {isLoading ? 'Выполняется вход...' : 'Войти'}
        </button>
      </form>

      <div className="mt-3 text-center">
        <p>Нет аккаунта? <Link to="/register">Зарегистрироваться</Link></p>
      </div>
    </div>
  );
};

export default LoginPage;
