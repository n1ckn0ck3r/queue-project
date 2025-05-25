import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { createQueue, updateQueue } from '../store/slices/queueSlice';

const QueueForm = ({ queue, onClose }) => {
  const dispatch = useDispatch();
  const { isLoading } = useSelector((state) => state.queue);
  const [formData, setFormData] = useState({
    name: '',
    disciplineId: '',
    active: true,
  });
  const [formErrors, setFormErrors] = useState({});
  
  // If queue is provided, it's an edit operation
  const isEditMode = !!queue;
  
  useEffect(() => {
    if (isEditMode && queue) {
      setFormData({
        name: queue.name || '',
        disciplineId: queue.discipline?.id || '',
        active: queue.active !== undefined ? queue.active : true,
      });
    }
  }, [isEditMode, queue]);
  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };
  
  const validateForm = () => {
    const errors = {};
    
    if (!formData.name.trim()) {
      errors.name = 'Queue name is required';
    }
    
    if (!formData.disciplineId) {
      errors.disciplineId = 'Discipline is required';
    }
    
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (validateForm()) {
      if (isEditMode) {
        dispatch(updateQueue({ id: queue.id, updates: formData }))
          .then((result) => {
            if (!result.error) {
              onClose();
            }
          });
      } else {
        dispatch(createQueue(formData))
          .then((result) => {
            if (!result.error) {
              onClose();
            }
          });
      }
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <div className="form-group">
        <label htmlFor="name" className="form-label">Queue Name</label>
        <input
          type="text"
          id="name"
          name="name"
          className="form-input"
          value={formData.name}
          onChange={handleChange}
        />
        {formErrors.name && <div className="form-error">{formErrors.name}</div>}
      </div>
      
      <div className="form-group">
        <label htmlFor="disciplineId" className="form-label">Discipline</label>
        <select
          id="disciplineId"
          name="disciplineId"
          className="form-input"
          value={formData.disciplineId}
          onChange={handleChange}
        >
          <option value="">Select a discipline</option>
          {/* We would normally fetch disciplines from the API and map them here */}
          <option value="1">Mathematics</option>
          <option value="2">Physics</option>
          <option value="3">Computer Science</option>
        </select>
        {formErrors.disciplineId && <div className="form-error">{formErrors.disciplineId}</div>}
      </div>
      
      <div className="form-group">
        <label className="form-checkbox-label">
          <input
            type="checkbox"
            name="active"
            checked={formData.active}
            onChange={handleChange}
          />
          <span className="ml-2">Active</span>
        </label>
      </div>
      
      <div className="form-group">
        <button type="submit" className="form-button" disabled={isLoading}>
          {isLoading ? (isEditMode ? 'Updating...' : 'Creating...') : (isEditMode ? 'Update Queue' : 'Create Queue')}
        </button>
      </div>
    </form>
  );
};

export default QueueForm;