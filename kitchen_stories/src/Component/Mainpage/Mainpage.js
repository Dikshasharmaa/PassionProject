import React, { useState } from 'react'
import './Mainpage.css';
import MealCard from '../MealCard/MealCard';



const Mainpage = () => {
  const [data, setData] = useState();
  const [input, setInput] = useState("");
  const[msg, setmsg] = useState("");

  const searchValue = (event) => {
    setInput(event.target.value)

  }
  const myFun = async () => {
    if(input == ""){
      setmsg("Please enter something")

    }
    else{
      const info = await fetch(`https://www.themealdb.com/api/json/v1/1/search.php?s=${input}`);
    const jsonData = await info.json()
    // console.log(jsonData.meals);
    setData(jsonData.meals)
    setmsg("")

    }
    
  }
  // console.log(data);
  return (
    <>
    <div className = 'container' >
        <div className='searchBar'>
            <input type='text' placeholder='What do yo want to eat today!' onChange={searchValue}/>
            <button onClick={myFun}>Search</button>
        </div>
        <h4 className='msg'>{msg}</h4>
        <div>
          <MealCard detail ={data}/>
        </div>
    </div>
    </>
    
  )
}

export default Mainpage