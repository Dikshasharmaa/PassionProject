import React, { useState } from 'react'
import './Mainpage.css';


const Mainpage = () => {
  const [data, setData] = useState();
  const [input, setInput] = useState();

  const searchValue = (event) => {
    setInput(event.target.value)

  }
  const myFun = async () => {
    const info = await fetch(`https://www.themealdb.com/api/json/v1/1/search.php?s=${input}`);
    const jsonData = await info.json()
    console.log(jsonData.meals);
    setData(jsonData.meals)
  }
  console.log(data);
  return (
    <>
    <div className = 'container' >
        <div className='searchBar'>
            <input type='text' placeholder='What do yo want to eat today!' onChange={searhValue}/>
            <button onClick={myFun}>Search</button>
        </div>
    </div>
    </>
    
  )
}

export default Mainpage