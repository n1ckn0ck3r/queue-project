import React from 'react';
import { Link } from 'react-router-dom';

const NotFoundPage = () => {
  return (
    <div className="text-center">
      <h1>404 - Страница не найдена</h1>
      <p className="mb-4">Страница, которую вы ищете, не существует.</p>
      <Link to="/" className="btn btn-primary">
        На главную
      </Link>
    </div>
  );
};

export default NotFoundPage;
