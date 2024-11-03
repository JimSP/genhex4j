import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, TextField, Box, Typography, Button, Grid, Paper, Accordion, AccordionSummary, AccordionDetails } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

const fetchInitialData = async () => {
  try {
    const response = await axios.get('https://mighty-nearly-minnow.ngrok-free.app/', {
      headers: {
        'Accept': 'application/json',
        'ngrok-skip-browser-warning': 'hi'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching initial data:', error);
    return {}; // Retorna um objeto vazio em caso de erro
  }
};


function ProjectForm() {
  const [formData, setFormData] = useState({});

  useEffect(() => {
    const getData = async () => {
      const data = await fetchInitialData();
      setFormData(data);
    };
    getData();
  }, []);

  const handleChange = (path, value) => {
    const keys = path.split('.');
    const newFormData = { ...formData };
    let current = newFormData;

    keys.forEach((key, index) => {
      if (index === keys.length - 1) {
        current[key] = value;
      } else {
        current = current[key];
      }
    });

    setFormData(newFormData);
  };

  const renderFields = (data, path = '') => {
    return Object.entries(data).map(([key, value]) => {
      const currentPath = path ? `${path}.${key}` : key;
      const displayName = key.replace(/_/g, ' ').toUpperCase();

      if (typeof value === 'object' && value !== null && !Array.isArray(value)) {
        return (
          <Accordion key={currentPath} sx={{ marginBottom: '1em', borderRadius: '5px', boxShadow: 1 }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />} aria-controls={`${currentPath}-content`} id={`${currentPath}-header`}>
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>{displayName}</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <Grid container spacing={2}>
                {renderFields(value, currentPath)}
              </Grid>
            </AccordionDetails>
          </Accordion>
        );
      } else if (Array.isArray(value)) {
        return (
          <Accordion key={currentPath} sx={{ marginBottom: '1em', borderRadius: '5px', boxShadow: 1 }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />} aria-controls={`${currentPath}-content`} id={`${currentPath}-header`}>
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>{displayName}</Typography>
            </AccordionSummary>
            <AccordionDetails>
              {value.map((item, index) => (
                <Box key={`${currentPath}.${index}`} sx={{ marginBottom: '1em', padding: '1em', border: '1px solid #ccc', borderRadius: '5px' }}>
                  <Typography variant="subtitle1" sx={{ fontWeight: 'bold', marginBottom: '0.5em' }}>{`${displayName} ${index + 1}`}</Typography>
                  <Grid container spacing={2}>
                    {typeof item === 'object' ? renderFields(item, `${currentPath}.${index}`) : (
                      <Grid item xs={12} key={`${currentPath}.${index}`} sx={{ marginBottom: '1em' }}>
                        <TextField
                          fullWidth
                          label={`${displayName} ${index + 1}`}
                          variant="outlined"
                          value={item || ''}
                          onChange={(e) => handleChange(`${currentPath}.${index}`, e.target.value)}
                          sx={{ bgcolor: '#ffffff', borderRadius: '5px' }}
                        />
                      </Grid>
                    )}
                  </Grid>
                </Box>
              ))}
            </AccordionDetails>
          </Accordion>
        );
      }

      return (
        <Grid item xs={12} key={currentPath} sx={{ marginBottom: '1em' }}>
          <TextField
            fullWidth
            label={displayName}
            variant="outlined"
            value={value || ''}
            onChange={(e) => handleChange(currentPath, e.target.value)}
            sx={{ bgcolor: '#ffffff', borderRadius: '5px' }}
          />
        </Grid>
      );
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/api/submit', formData);
      console.log('Form submitted successfully:', response.data);
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ bgcolor: '#ffffff', padding: '2em', borderRadius: '10px', boxShadow: 3 }}>
        <Typography variant="h4" gutterBottom sx={{ textAlign: 'center', marginBottom: '1.5em', fontWeight: 'bold', color: '#333' }}>
          Genhex4j :: powered by ene3xt.ai
        </Typography>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            {renderFields(formData)}
          </Grid>
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ padding: '1em', marginTop: '2em', bgcolor: '#1976d2' }}>
            Submit
          </Button>
        </form>
      </Box>
    </Container>
  );
}

export default ProjectForm;
