import React from 'react'
import { useParams } from 'react-router'

const Mealinfo = () => {
    const {mealid} = useParams();
    console.log(mealid);
  return (
    <div>
        Mealinfo
        </div>
  )
}

export default Mealinfo