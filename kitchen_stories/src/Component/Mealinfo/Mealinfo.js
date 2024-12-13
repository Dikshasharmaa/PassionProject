import React from 'react'
import { useParams } from 'react-router'

const Mealinfo = () => {
    const {mealid} = useParams();
    console.log(mealid);
    const getInfo = async () =>{
      const get = await fetch(`https://www.themealdb.com/api/json/v1/1/lookup.php?i= ${mealid}`);
      const jsonData = await get.json();
      console.log(jsonData);
    }
    if(mealid != ""){
      getInfo()
    }
  return (
    <div>
        Mealinfo
        </div>
  )
}

export default Mealinfo