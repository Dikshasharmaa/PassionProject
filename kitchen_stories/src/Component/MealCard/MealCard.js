import { map } from 'lodash';
import React from 'react';
import './MealCard.css';

const MealCard = ({detail}) => {
    console.log(detail);
  return (
    <div className='meals'>
        {!detail ? "" : detail.map((curItem) =>{
            return(
                <div className='mealImg'> 
                    <img src ={curItem.strMealThumb}></img>
                    <h2>{curItem.strMeal}</h2>
                    <button>Recipe</button>
                </div>

            )
        })}
        </div>
  )
}

export default MealCard