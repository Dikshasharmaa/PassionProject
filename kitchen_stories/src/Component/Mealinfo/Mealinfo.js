import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router';
import './Mealinfo.css';

const Mealinfo = () => {
  const { mealid } = useParams(); // get mealid from URL
  const [info, setInfo] = useState(null); // Create state variable 'info' and set its default value to null

  useEffect(() => {
    const getInfo = async () => { //fetching data from API using mealid
      try {
        const response = await fetch(`https://www.themealdb.com/api/json/v1/1/lookup.php?i=${mealid}`);
        const jsonData = await response.json();
        if (jsonData.meals) {
          setInfo(jsonData.meals[0]);  // Store the fetched meal data in info
        } else {
          console.error('No meals found');  // If no meal data is returned
        }
      } catch (error) {
        console.error('Error fetching meal data:', error); // if something goes wrong while fetching
      }
    };

    if (mealid) {
      getInfo();
    }
  }, [mealid]);

  if (!info) {
    return <div>Loading...</div>; // This check avoids trying to display undefined data and shows a "Loading"message
  }

  //Extracting ingregients and it's measurments
  const ingregients =[];  //declaring an array of ingredients
  for(let i =1; i<=20; i++){
    const ingredient = info[`strIngredient${i}`];
    const measure = info[`strMeasure${i}`];
    if(ingredient && ingredient.trim()){  // checking for valid ingredient
      ingregients.push(`${ingredient} - ${measure}`); // combining ingredient and it's measure
      
    } 
  }


  return (
    <div className='mealInfo'>
      <img src={info.strMealThumb} alt={info.strMeal} />
      <div className='info'>
        <h1>Recipe Details</h1>
        <button>{info.strMeal}</button>
       <ul>
        {ingregients.map((item, index) =>(
          <li key={index}>{item}</li>
        ))}
        </ul>
        <h3>Instructions</h3>
        <p>{info.strInstructions}</p>
      </div>
    </div>
  );
};

export default Mealinfo;
