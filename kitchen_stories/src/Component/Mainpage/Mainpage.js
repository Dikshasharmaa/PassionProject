import React from 'react'
import './Mainpage.css';


const Mainpage = () => {
  const myFun = async () => {
    const data = await fetch(`https://www.themealdb.com/api/json/v1/1/search.php?s=cake`);
    console.log(data);
  }
  return (
    <>
    <div className = 'container' >
        <div className='searchBar'>
            <input type='text' placeholder='What do yo want to eat today!'/>
            <button onClick={myFun}>Search</button>
        </div>
    </div>
    </>
    
  )
}

export default Mainpage