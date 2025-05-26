import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { createQueue, updateQueue } from '../store/slices/queueSlice';
import { fetchDisciplines, resetAdminState } from '../store/slices/adminSlice';

const QueueForm = ({ queue, onClose }) => {
  const dispatch = useDispatch();
  const { isLoading } = useSelector((state) => state.queue);
  const { disciplines } = useSelector((state) => state.admin);

  const getLocalDateTime = (isoString) => {
    return isoString.slice(0, 16);
  }

  const [formData, setFormData] = useState({
    discipline: {
      id: 0,
      disciplineName: ''
    },
    queueStart: getLocalDateTime(new Date().toISOString()),
    queueEnd: getLocalDateTime(new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString()),
  });
  const [formErrors, setFormErrors] = useState({});

  // If queue is provided, it's an edit operation
  const isEditMode = !!queue;

  useEffect(() => {
    if (isEditMode && queue) {
      setFormData({
        discipline: {
          id: queue.discipline.id,
          disciplineName: queue.discipline.disciplineName
        },
        queueStart: getLocalDateTime(queue.queueStart),
        queueEnd: getLocalDateTime(queue.queueEnd),
      });
      
    }
  }, [isEditMode, queue]);

  useEffect(() => {
    dispatch(resetAdminState());
    dispatch(fetchDisciplines());
  }, [dispatch]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    if (name === 'disciplineId') {
      const selected = disciplines.find((d) => d.id === Number(value)) || { id: 0, disciplineName: '' };
      setFormData((prev) => ({
        ...prev,
        discipline: {
          id: selected.id,
          disciplineName: selected.disciplineName,
        },
      }));
    } else if (type === 'checkbox') {
      setFormData((prev) => ({ ...prev, [name]: checked }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const validateForm = () => {
    const errors = {};

    if (!formData.discipline.disciplineName) {
      errors.discipline = 'Дисциплина обязательна';
    }

    if (!formData.queueStart) {
      errors.queueStart = 'Время начала обязательно';
    }

    if (!formData.queueEnd) {
      errors.queueEnd = 'Время окончания обязательно';
    }

    if (formData.queueStart && formData.queueEnd 
      && new Date(formData.queueStart) >= new Date(formData.queueEnd)) {
        errors.queueEnd = 'Время окончания должно быть позже времени начала';
      }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    const payload = {
      discipline: {
        id: formData.discipline.id,
        disciplineName: formData.discipline.disciplineName,
      },
      queueStart: new Date(formData.queueStart).toISOString(),
      queueEnd: new Date(formData.queueEnd).toISOString(),
      active: true,
    };

    const action = isEditMode 
      ? updateQueue({ id: queue.id, updates: payload })
      : createQueue(payload);

    dispatch(action).then((res) => {
      if (!res.error) onClose();
    })
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="form-group">
        <label htmlFor="disciplineId" className="form-label">Дисциплина</label>
        <select
          id="disciplineId"
          name="disciplineId"
          className="form-input"
          value={formData.discipline.id}
          onChange={handleChange}
        >
          <option value="">Выберите дисциплину</option>
          {/* We would normally fetch disciplines from the API and map them here */}
          {disciplines.map((d) => <option key={d.id} value={d.id}>{d.disciplineName}</option>)}
        </select>
        {formErrors.discipline && <div className="form-error">{formErrors.discipline}</div>}
      </div>

      <div className="form-group">
        <label htmlFor="queueStart" className="form-label">Начало очереди</label>
        <input 
          type="datetime-local"
          id="queueStart"
          name="queueStart"
          className="form-input"
          value={formData.queueStart}
          onChange={handleChange}
        />
          {formErrors.queueStart && <div className="form-error">{formErrors.queueStart}</div>}
      </div>

      <div className="form-group">
        <label htmlFor="queueEnd" className="form-label">Окончание очереди</label>
        <input 
          type="datetime-local"
          id="queueEnd"
          name="queueEnd"
          className="form-input"
          value={formData.queueEnd}
          onChange={handleChange}
        />
          {formErrors.queueEnd && <div className="form-error">{formErrors.queueEnd}</div>}
      </div>


      <div className="form-group">
        <button type="submit" className="form-button" disabled={isLoading}>
          {isLoading ? (isEditMode ? 'Обновление...' : 'Создание...') : (isEditMode ? 'Обновить очередь' : 'Создать очередь')}
        </button>
      </div>
    </form>
  );
};

export default QueueForm;
